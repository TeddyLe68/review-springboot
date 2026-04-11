package com.teddy.youtuberef.service.mapper;

import com.teddy.youtuberef.entity.AccountEntity;
import com.teddy.youtuberef.service.dto.AccountDto;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class
)
public interface AccountMapper extends EntityMapper<AccountDto, AccountEntity> {
}
