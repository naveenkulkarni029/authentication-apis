package com.kn.wallets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kn.wallets.domain.Wallet;

@Repository
public interface WalletsRepository extends JpaRepository<Wallet, Long>{

}
