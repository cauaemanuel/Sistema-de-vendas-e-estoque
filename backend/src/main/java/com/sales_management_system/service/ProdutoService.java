package com.sales_management_system.service;

import com.sales_management_system.Repository.ProdutoRepository;
import com.sales_management_system.controller.dto.ProdutoDTO;
import com.sales_management_system.entity.Produto;
import com.sales_management_system.mapper.ProdutoMap;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class ProdutoService {

    private ProdutoRepository produtoRepository;
    private ProdutoMap produtoMap;

    public ProdutoService(ProdutoRepository produtoRepository, ProdutoMap produtoMap) {
        this.produtoRepository = produtoRepository;
        this.produtoMap = produtoMap;
    }

    @Transactional
    public Produto create(ProdutoDTO dto){
        var produto = produtoMap.fromDTO(dto);
        return produtoRepository.save(produto);
    }

    public List<ProdutoDTO> findAll(){
        var produtos = produtoRepository.findAll().stream()
                .map(p -> produtoMap.fromProduto(p)).toList();
        if(produtos.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return produtos;
    }

    @Transactional
    public ProdutoDTO findById(String id){
        var produto = findEntityByid(id);
        return produtoMap.fromProduto(produto);
    }

    @Transactional
    public void deleteById(String id){
        var produto = findEntityByid(id);
        produtoRepository.deleteById(produto.getId());
    }

    public ProdutoDTO updateProduto(ProdutoDTO dto, String id){
        var produto = findEntityByid(id);
        var produtoUpdate = produtoMap.updateFromDTO(dto, produto);
        return produtoMap.fromProduto(produtoRepository.save(produtoUpdate));
    }

    protected Produto findEntityByid(String id){
        var uuid = UUID.fromString(id);
        return produtoRepository.findById(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado; Verifique o ID"));
    }

    protected void save (Produto produto) {
        produtoRepository.save(produto);
    }
}