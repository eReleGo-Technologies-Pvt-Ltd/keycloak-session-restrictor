package dasniko.keycloak.events;

import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.adapter.InMemoryUserAdapter;

/**
 * @author Niko Köbler, https://www.n-k.de, @dasniko
 */
public class SessionRestrictorEventListenerProvider implements EventListenerProvider {

    private final KeycloakSession keycloakSession;

    public SessionRestrictorEventListenerProvider(KeycloakSession keycloakSession) {
        this.keycloakSession = keycloakSession;
    }

    @Override
    public void onEvent(Event event) {
        if (EventType.LOGIN.equals(event.getType()) && !event.getDetails().containsKey("response_type")) {
			String clientId = keycloakSession.getContext().getClient().getClientId();
			if (!clientId.equals("admin-cli")) {
				RealmModel realm = keycloakSession.getContext().getRealm();
				InMemoryUserAdapter user = new InMemoryUserAdapter(keycloakSession, realm, event.getUserId());
				keycloakSession.sessions().getUserSessionsStream(realm, user).forEach(userSession -> {
					// remove all existing user sessions but the current one (last one wins)
					// this is HIGHLANDER MODE - there must only be one!
					if (!userSession.getId().equals(event.getSessionId())) {
						keycloakSession.sessions().removeUserSession(realm, userSession);
						event.setError("older sessions removed -> multiple sessions found -> clientId:" + clientId);
					}
				});
			}
        }
    }

    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) {
    }

    @Override
    public void close() {
    }
}
