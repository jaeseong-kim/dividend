package com.example.dividend.service;

import com.example.dividend.exception.impl.NoCompanyException;
import com.example.dividend.model.Company;
import com.example.dividend.model.Dividend;
import com.example.dividend.model.ScrapedResult;
import com.example.dividend.model.constant.CacheKey;
import com.example.dividend.persist.CompanyRepository;
import com.example.dividend.persist.DividendRepository;
import com.example.dividend.persist.entity.CompanyEntity;
import com.example.dividend.persist.entity.DividendEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FinanceService {

	private final CompanyRepository companyRepository;
	private final DividendRepository dividendRepository;

	@Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
	public ScrapedResult getDividendByCompanyName(String companyName) {

		// 1. 회사명으로 회사 정보 조회
		CompanyEntity companyEntity = this.companyRepository.findByName(companyName)
			.orElseThrow(() -> new NoCompanyException());

		// 2. 조회된 회사 아이디로 배당금 조회
		List<DividendEntity> dividendEntities = this.dividendRepository.findAllByCompanyId(
			companyEntity.getId());

		// 3. 결과 조합
		List<Dividend> dividends = new ArrayList<>();
		for (var entity : dividendEntities) {
			dividends.add(new Dividend(entity.getDate(), entity.getDividend()));
		}

		return new ScrapedResult(new Company(companyEntity.getTicker(), companyEntity.getName())
			, dividends);
	}
}
