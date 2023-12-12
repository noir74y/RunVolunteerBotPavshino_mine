package telegram.bot.storage;

import org.junit.jupiter.api.Test;
import telegram.bot.config.BotConfiguration;
import telegram.bot.model.Participation;
import telegram.bot.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrganizerInformerTest {
    private final String orgInfo = "119649111" + ";" + "molyavkin" + System.lineSeparator();
    private final String orgInfoOther = "111111111" + ";" + "username" + System.lineSeparator();
    private final String emptyOrgInfoSep = System.lineSeparator();
    private final String emptyOrgInfo = "";
    private static final LocalDate eventDate = LocalDate.parse("11.11.2023", BotConfiguration.DATE_FORMATTER);

    private User createOrg() {
        return User.builder()
                .name("Петя")
                .surname("ИВАНОВ" + " " + LocalDateTime.now().format(DateTimeFormatter.ISO_TIME))
                .telegram("molyavkin")
                .code("0000").build();
    }

    private User createOtherOrg() {
        return User.builder()
                .name("Вася")
                .surname("ПЕТРОВ" + " " + LocalDateTime.now().format(DateTimeFormatter.ISO_TIME))
                .telegram("username")
                .code("0000").build();
    }

    private Participation createParticipation(User user, int rowNumber) {
        return Participation.builder()
                .user(user)
                .eventDate(eventDate)
                .eventRole("Организатор")
                .sheetRowNumber(rowNumber)
                .build();
    }

    private List<String> createOrganizers() {
        List<String> organizers = new ArrayList<>();
        organizers.add(orgInfo);
        organizers.add(orgInfoOther);
        organizers.add(emptyOrgInfo);
        organizers.add(emptyOrgInfoSep);

        return organizers;
    }

    @Test
    void isListContainsLineTest() {
        List<String> organizers = createOrganizers();
        assertTrue(OrganizerInformer.isListContainsUserName(organizers, "molyavkin"));
        assertFalse(OrganizerInformer.isListContainsUserName(organizers, "olyavkin"));
        assertTrue(OrganizerInformer.isListContainsUserName(organizers, "username"));
        assertFalse(OrganizerInformer.isListContainsUserName(organizers, "usernam"));

        assertTrue(OrganizerInformer.isListContainsUserId(organizers, "119649111"));
        assertFalse(OrganizerInformer.isListContainsUserId(organizers, "11964911"));
        assertTrue(OrganizerInformer.isListContainsUserId(organizers, "111111111"));
        assertFalse(OrganizerInformer.isListContainsUserId(organizers, "11111111"));
    }

    @Test
    void getOrganizersIdsTelegramTest() {
//        User organizer = createOrg();
//        User otherOrganizer = createOtherOrg();
//        Participation participationOrg = createParticipation(organizer, 2);
//        Participation participationPhoto = createParticipation(otherOrganizer, 3);
//        List<Participation> participations = new ArrayList<>();
//        participations.add(participationOrg);
//        participations.add(participationPhoto);
//
//        List<Long> organizersIdsTelegram = OrganizerInformer.getOrganizersIdsTelegram(participations);
//
//        assertEquals(organizersIdsTelegram.size(), 1);
    }

    @Test
    void idFromListOrgInfo() {
        List<String> organizers = new ArrayList<>();
        organizers.add(orgInfo);
        organizers.add(orgInfoOther);
        Long id = OrganizerInformer.idFromListOrgInfo(organizers, "molyavkin");
        assertEquals(id, 119649111);

        id = OrganizerInformer.idFromListOrgInfo(organizers, "username");
        assertEquals(id, 111111111);

        id = OrganizerInformer.idFromListOrgInfo(organizers, "unknown");
        assertEquals(id, 0L);
    }

    @Test
    void userNameFromOrgInfo() {
        assertEquals(OrganizerInformer.userNameFromOrgInfo(orgInfo), "molyavkin");
        assertEquals(OrganizerInformer.userNameFromOrgInfo(orgInfoOther), "username");
        assertEquals(OrganizerInformer.userNameFromOrgInfo(emptyOrgInfoSep), "");
        assertEquals(OrganizerInformer.userNameFromOrgInfo(emptyOrgInfo), "");
    }
}