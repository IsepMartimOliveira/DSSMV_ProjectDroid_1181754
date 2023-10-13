package com.example.onlinesupertmarket.Mapper;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DTOMapper<D, M> {
    private Function<D, M> dtoToModelMapper;

    public DTOMapper(Function<D, M> dtoToModelMapper) {
        this.dtoToModelMapper = dtoToModelMapper;
    }

    public M map(D dto) {
        return dtoToModelMapper.apply(dto);
    }

    public List<M> mapList(List<D> dtos) {
        return dtos.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }
}
