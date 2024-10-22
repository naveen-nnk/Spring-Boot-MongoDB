package com.responsive.naveen.assignment.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.responsive.naveen.assignment.demo.model.TradingStocks;

@Repository
public interface TradingRepository extends MongoRepository<TradingStocks, Long>{

}
