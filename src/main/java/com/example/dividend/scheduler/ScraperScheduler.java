package com.example.dividend.scheduler;

import com.example.dividend.model.Company;
import com.example.dividend.model.ScrapedResult;
import com.example.dividend.model.constant.CacheKey;
import com.example.dividend.persist.CompanyRepository;
import com.example.dividend.persist.DividendRepository;
import com.example.dividend.persist.entity.CompanyEntity;
import com.example.dividend.persist.entity.DividendEntity;
import com.example.dividend.scraper.Scraper;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@EnableCaching
@Slf4j
public class ScraperScheduler {

	private final CompanyRepository companyRepository;
	private final DividendRepository dividendRepository;
	private final Scraper yahooFinanceScraper;

	@CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true)
	@Scheduled(cron = "${scheduler.scrap.yahoo}")
	public void yahooFinanceScheduler() {
		log.info("scraping scheduler is started");
		List<CompanyEntity> companyEntities = companyRepository.findAll();

		for (var company : companyEntities) {
			ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(new Company(
				company.getTicker(), company.getName()));

			scrapedResult.getDividendEntities().stream()
				.map(e -> new DividendEntity(company.getId(), e))
				.forEach(e -> {
						boolean exists = this.dividendRepository.existsByCompanyIdAndDate(
							e.getCompanyId(), e.getDate());
						if (!exists) {
							this.dividendRepository.save(e);
							log.info("insert new dividend ->" + e);
						}
					}
				);

			//스크래핑 대상 사이트에 연속적으로 날리지 않도록
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
