package hu.evosoft.model.test;

import static org.junit.Assert.assertTrue;
import hu.evosoft.model.DestinationHost;
import hu.evosoft.model.IMongoModel;
import hu.evosoft.model.LogEntryDate;
import hu.evosoft.model.MongoModelList;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class MongoModelListTest {

	@Test
	public void testGetList() {
		List<IMongoModel> list = MongoModelList.getModelSet();
		List<IMongoModel> testList = new ArrayList<>();
		testList.add(new DestinationHost());
		testList.add(new LogEntryDate());
		for (IMongoModel testModel : testList){
			boolean isFound = false;
			for (IMongoModel model : list) {
				if (model.getClass().isInstance(testModel) && model.equals(testModel)) {
					isFound = true;
				}
			}
			assertTrue(MessageFormat.format("{0} not found in the MongoModelList", testModel.collectionName()), isFound);
		}
	}

}
