package Repositories;

import Domain.Entity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PaginatedRepo<ID,E extends Entity<ID>> extends PagingAndSortingRepository<E,ID> {
    public Page<E> findAll(Pageable pageable);
}

