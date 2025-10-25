package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Timezone;

public interface TimezoneRepository extends JpaRepository<Timezone, Long> {

	Optional<Timezone> findById(Long timezoneId);

}
