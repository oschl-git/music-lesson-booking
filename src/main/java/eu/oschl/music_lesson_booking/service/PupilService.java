package eu.oschl.music_lesson_booking.service;

import eu.oschl.music_lesson_booking.dto.Pupil;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@Service
public class PupilService {

    private final RestClient client;
    private final JsonMapper mapper;

    public PupilService(JsonMapper mapper) {
        this.mapper = mapper;

        this.client = RestClient.create(
            "https://pmaier.eu.pythonanywhere.com/plt"
        );
    }

    public Pupil getPupilByMatric(int matric) {
        JsonNode response = client
            .get()
            .uri("/pupil/{matric}", matric)
            .retrieve()
            .body(JsonNode.class);

        return mapper.treeToValue(
            response.get("pupil"),
            Pupil.class
        );
    }
}
