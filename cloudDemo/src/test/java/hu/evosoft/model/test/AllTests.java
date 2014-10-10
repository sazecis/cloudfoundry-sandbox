package hu.evosoft.model.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AbstractMongoModelTest.class, DestinationHostTest.class,
		LogEntryDateTest.class, MongoModelListTest.class })
public class AllTests {

}
