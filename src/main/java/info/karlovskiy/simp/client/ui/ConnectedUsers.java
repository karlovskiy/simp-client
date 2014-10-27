package info.karlovskiy.simp.client.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 10/21/14
 */
public class ConnectedUsers {

    private Set<String> connectedUsers = new TreeSet<>();

    public void init(Collection<String> users) {
        connectedUsers.clear();
        connectedUsers.addAll(users);
    }

    public void add(String user) {
        connectedUsers.add(user);
    }

    public void remove(String user) {
        connectedUsers.remove(user);
    }

    public String asText() {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = connectedUsers.iterator();
        if (it.hasNext()) {
            for (; ; ) {
                String e = it.next();
                sb.append(e);
                if (!it.hasNext())
                    break;
                sb.append('\n');
            }
        }
        return sb.toString();
    }

}
