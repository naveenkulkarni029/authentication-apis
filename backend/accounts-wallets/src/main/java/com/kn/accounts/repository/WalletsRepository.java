package com.kn.accounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kn.accounts.domain.Wallet;

@Repository
public interface WalletsRepository extends JpaRepository<Wallet, Long>{

}
