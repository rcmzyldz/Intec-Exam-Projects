package be.intec.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @parametized_type E is het datatype van entiteit.
 * @parametized_type ID is het datatype van de primaire-sleutel
 * */
public interface IMessageRepository<Message, Long> {
    /**
     * @param e Nieuwe entiteit
     * @return Laatste gecreëerde primaire-sleutel.
     */
    Long save(Message message);
    /**
     * De methode is default, dus GEEN verplicht om over te schrijven.
     * @param eList Een verzameling van entiteiten.
     * @return Een verzameling van succes gecreëerde primaire-sleutels.
     * Als er 10 elementen zijn toegevoegd maar 4 waren succes,
     * de collectie gaat bevatten enkel 4 ID's.
     */
    default List<Long> saveAll(List<Message> messageList){
        final List<Long> savedIdList = new ArrayList<>();
        for (Message message : messageList) {
            Long nextSavedId = save(message);
            savedIdList.add(nextSavedId);
        }
        return savedIdList;
    }
    /**
     * @param id Primaire-sleutel van het bericht.
     * @param content De titel van het bericht.
     * @return Het laatst bijgewerkt primair-sleutel
     */
    Long updateSubject(Long id, String content);
    /**
     * @param id Primaire-sleutel van het bericht.
     * @param content De body van het bericht.
     * @return Het laatst bijgewerkt primair-sleutel
     */
    Long updateContent(Long id, String content);
    /**
     * @param id Primaire-sleutel van het bericht.
     * @return Het laatst verwijderde primair-sleutel
     */
    Long deleteById(Long id);
    /**
     * @param id Primaire-sleutel van het bericht.
     * @return Het bericht matcht met PS (Primaire-Sleutel).
     */
    Optional<be.intec.models.Message> findById(Long id);
    /**
     * @param limit Hoeveelheid van de entiteiten dia gaat retourneert worden.
     * @param offset Pagina's nummer.
     * @return Een verzameling van berichten.
     * Bijvoorbeeld als de user-invoer limit met 10 offset met 2 is,
     * dan geeft de methode terug 10 resultaten van de 2de pagina.
     */
// OPTIONEEL
    List<Message> findAll(Long limit, Long offset);
    List<Message> findAllBySender(Long fromUserId);
    List<Message> findAllFromReceiver(Long toUserId);
}