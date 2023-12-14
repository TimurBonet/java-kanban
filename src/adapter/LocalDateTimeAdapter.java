package adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm_dd.MM.yyyy");
    private static final DateTimeFormatter readFormatter = DateTimeFormatter.ofPattern("HH-mm_dd.MM.yyyy");
    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
        if (localDateTime == null) {
            System.out.println(jsonWriter.toString());
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(localDateTime.format(formatter)/*localDateTime.toString()*/);
        }
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        System.out.println(jsonReader.toString());
        if (jsonReader.peek() == JsonToken.NULL) {
            System.out.println(jsonReader.toString());
            jsonReader.nextNull();
            return null;
        }
        return LocalDateTime.parse(jsonReader.nextString(), formatter);
    }
}
