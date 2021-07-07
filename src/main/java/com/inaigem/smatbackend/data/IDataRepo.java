package com.inaigem.smatbackend.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDataRepo extends JpaRepository<Data, Integer> {

}
