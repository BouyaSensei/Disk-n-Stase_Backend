package com.stase.services;

import org.springframework.stereotype.Service;

import com.stase.repositories.LibrairyRepository;

@Service
public class LibrairyService {
    private LibrairyRepository librairyRepository;

    public LibrairyService(LibrairyRepository librairyRepository) {
        this.librairyRepository = librairyRepository;
    }

}
