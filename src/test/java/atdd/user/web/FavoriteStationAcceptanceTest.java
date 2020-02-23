package atdd.user.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.web.StationHttpTest;
import atdd.user.application.dto.CreateFavoriteStationRequestView;
import atdd.user.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import static atdd.Constant.AUTH_SCHEME_BEARER;
import static atdd.path.TestConstant.STATION_NAME;

public class FavoriteStationAcceptanceTest extends AbstractAcceptanceTest {
    public static final String FAVORITE_STATION_BASE_URI = "/favorite-station";
    public static final String NAME = "브라운";
    public static final String EMAIL = "boorwonie@email.com";
    public static final String PASSWORD = "subway";
    private UserHttpTest userHttpTest;
    private StationHttpTest stationHttpTest;
    private Long stationId;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
        this.stationHttpTest = new StationHttpTest(webTestClient);
        userHttpTest.createUser(EMAIL, NAME, PASSWORD);
    }

    @Test
    public void createFavoriteStation() {
        //given
        String token = jwtTokenProvider.createToken(EMAIL);
        this.stationId = stationHttpTest.createStation(STATION_NAME);

        //when
        CreateFavoriteStationRequestView request = new CreateFavoriteStationRequestView(stationId);

        //then
        webTestClient.post().uri(FAVORITE_STATION_BASE_URI)
                .header("Authorization", AUTH_SCHEME_BEARER + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateFavoriteStationRequestView.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("Location")
                .expectBody().jsonPath("$.userEmail").isEqualTo(EMAIL)
                .jsonPath("$.id").isEqualTo(1);
    }
}