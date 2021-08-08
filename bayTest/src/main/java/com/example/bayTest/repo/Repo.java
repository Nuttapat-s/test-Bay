package com.example.bayTest.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bayTest.model.AllData;

@Repository
public interface Repo extends JpaRepository<AllData, Long> {

}
