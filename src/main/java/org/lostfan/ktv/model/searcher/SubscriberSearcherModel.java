package org.lostfan.ktv.model.searcher;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;

public class SubscriberSearcherModel extends EntitySearcherModel<Subscriber> {

    private enum QueryParseStates {
        Begin,
        Name,
        House,
        Flat,
        End;
    }

    private List<EntityField> fields;
    private SubscriberDAO subscriberDAO;

    public SubscriberSearcherModel() {
        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("subscriber.account", EntityFieldTypes.Integer, Subscriber::getAccount, Subscriber::setAccount));
        this.fields.add(new EntityField("subscriber.name", EntityFieldTypes.String, Subscriber::getName, Subscriber::setName));
        this.fields.add(new EntityField("subscriber.street_id", EntityFieldTypes.Street, Subscriber::getStreetId, Subscriber::setStreetId));
        this.fields.add(new EntityField("subscriber.balance", EntityFieldTypes.Integer, Subscriber::getBalance, Subscriber::setBalance));
        this.fields.add(new EntityField("subscriber.connected", EntityFieldTypes.Boolean, Subscriber::isConnected, Subscriber::setConnected));

        this.subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    }

    @Override
    public void setSearchQuery(String query) {
        // Replace commas with the space key
        query = query.toLowerCase().replaceAll(",", " ");

        setList(this.subscriberDAO.search(parseQuery(query)));
    }

    /**
     * A smart search implementation
     */
    private SubscriberSearchCriteria parseQuery(String query) {
        SubscriberSearchCriteria criteria = new SubscriberSearchCriteria();

        StringTokenizer tokenizer = new StringTokenizer(query, " ");

        // The query contains just one word
        if (tokenizer.countTokens() == 1) {
            String name = tokenizer.nextToken();
            criteria.addName(name);
            criteria.addStreet(name);
            return criteria;
        }

        QueryParseStates lastState = QueryParseStates.Begin;
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();

            switch (lastState) {
                case Begin:
                    criteria.addName(token);
                    criteria.addStreet(token);
                    lastState = QueryParseStates.Name;
                    break;
                case Name:
                    // Try to retrieve a house number
                    int i;
                    for (i = 0; i < token.length(); ++i) {
                        if (!Character.isDigit(token.charAt(i))) {
                            break;
                        }
                    }
                    // House numbers start with a digit
                    if (i != 0) {
                        criteria.setHouse(Integer.parseInt(token.substring(0, i)));
                        if (i != token.length()) {
                            criteria.setIndex(token.substring(i, token.length()));
                        }
                        // remove subscriber name from the criteria
                        criteria.setNameIn(new ArrayList<>());
                        lastState = QueryParseStates.House;

                    }
                    // Otherwise it's another word of a name
                    else {
                        criteria.addStreet(token);
                        criteria.addName(token);
                    }
                    break;
                case House:
                    // Next token is a flat number
                    criteria.setFlat(token);
                    lastState = QueryParseStates.Flat;
                    break;
                case Flat:
                    // Possibly the "flat" value is actually a building value
                    // And the next value is true flat value
                    criteria.setBuilding(criteria.getFlat());
                    criteria.setFlat(token);
                    break;
                case End:
                    // ???
                    // What to do next?
                    break;
            }

        }

        return criteria;
    }

    @Override
    public Class getEntityClass() {
        return Subscriber.class;
    }

    @Override
    public String getEntityNameKey() {
        return "subscribers";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    @Override
    protected EntityDAO<Subscriber> getDao() {
        return this.subscriberDAO;
    }
}
