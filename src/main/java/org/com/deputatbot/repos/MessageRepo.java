package org.com.deputatbot.repos;

import com.boot.lions.domain.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepo extends CrudRepository<Message, Long> {

List<Message> findByTag(String tag);
List<Message> findByText(String text);

}
