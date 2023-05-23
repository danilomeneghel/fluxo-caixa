package api.repository;

import api.entity.ProdutoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends CrudRepository<ProdutoEntity, Long> {

    List<ProdutoEntity> findAll();

}
