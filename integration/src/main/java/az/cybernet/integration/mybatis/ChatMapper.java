package az.cybernet.integration.mybatis;

import az.cybernet.integration.dto.ChatDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMapper {
    void insertChat(ChatDTO chat);

    List<String> findChatIdByPhone(String phone);
}
