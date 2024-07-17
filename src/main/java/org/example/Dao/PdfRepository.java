package org.example.Dao;


import org.example.Model.StoredPdf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PdfRepository extends JpaRepository<StoredPdf, Long> {

}

