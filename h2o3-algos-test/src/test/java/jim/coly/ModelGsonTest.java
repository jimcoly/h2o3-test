package jim.coly;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import hex.deeplearning.DeepLearningModel;
import water.Key;

public class ModelGsonTest {

	@Test
	public void test1() throws Exception {

		String fileName = "src/test/resources/model/model.json";
		String jsonString = new String(Files.readAllBytes(Paths.get(fileName)));
		// System.out.println(jsonString);

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Key.class, new KeyDeserializer());
		Gson gson = gsonBuilder.create();
		DeepLearningModel model = gson.fromJson(jsonString, DeepLearningModel.class);
		System.out.println(model._key);

	}
	
	class KeyDeserializer implements JsonDeserializer<Key>{

		@Override
		public Key deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			if (json != null) {
				JsonObject keyJsonObj = json.getAsJsonObject();
				String keyName = keyJsonObj.get("name").getAsString();
				return Key.make(keyName);
			}
			return null;
		}
		
	}

}
