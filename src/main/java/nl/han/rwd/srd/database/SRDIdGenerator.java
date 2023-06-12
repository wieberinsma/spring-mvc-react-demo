package nl.han.rwd.srd.database;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;

public class SRDIdGenerator extends UUIDGenerator
{
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) throws HibernateException
    {
        if (object == null)
        {
            throw new HibernateException(new NullPointerException());
        }

        final Object id = session.getEntityPersister(null, object).getClassMetadata().getIdentifier(object,
                session);
        return id == null ? super.generate(session, object) : id;
    }
}
