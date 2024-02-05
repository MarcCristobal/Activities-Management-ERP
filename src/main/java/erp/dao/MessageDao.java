package erp.dao;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import erp.domain.Message;


public interface MessageDao extends GenericDao<Message, Long> {
    @Query("SELECT m FROM Message m JOIN m.userRecipients u WHERE u.id = :recipientId")
    List<Message> findMessagesByUserRecipients(@Param("recipientId") Long recipientId);
}
