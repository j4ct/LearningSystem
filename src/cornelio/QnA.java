package cornelio;

/* Questions and Answers class */

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

public class QnA
{
    Map<String, TreeMap> qna;

    QnA (String fileName)
    {
        this.qna = jsonToObject(fileName);
    }

    private Map<String, TreeMap> jsonToObject (String fileName)
    {
        String data = "";
        String line;

        // Reading file.
        try
        {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            while ((line = br.readLine()) != null) {
                data += line + "\n";
            }

            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // Set the object (Map<String, Object>) with the json data.
        return new GsonBuilder().setPrettyPrinting().create().fromJson(data, new TypeToken<Map<String, TreeMap>>(){}.getType());
    }

    public String getQuestionByIndex(Integer index)
    {
        return (String) this.qna.get(index.toString()).get("question");
    }

    public String getAnswerByIndex(Integer index)
    {
        return (String) this.qna.get(index.toString()).get("answer");
    }

    public void setQuestionByIndex(Integer index, String question)
    {
        this.qna.get(index.toString()).replace("question", question);
    }

    public void setAnswerByIndex(Integer index, String answer)
    {
        this.qna.get(index.toString()).replace("answer", answer);
    }

    public void addByIndex(Integer currentIndex)
    {
        Map<String, TreeMap> split = new LinkedTreeMap<>();
        //TreeMap<String, String> blank = (TreeMap<String, String>) Map.of("question", "", "answer", "");

        for (int i = 0, j = 0; i < this.qna.size() + 1; i++, j++)
        {
            split.put(String.valueOf(i + 1), this.qna.get(String.valueOf(j + 1)));

            if (i + 1 == currentIndex)
            {
                split.put(String.valueOf(currentIndex + 1), new TreeMap<String, String>());
                split.get(String.valueOf(currentIndex + 1)).put("question", "");
                split.get(String.valueOf(currentIndex + 1)).put("answer", "");
                i++;
            }
        }

        this.qna = split;

        //System.out.println(this.qna);
    }

    public void removeByIndex(Integer currentIndex)
    {
        Map<String, TreeMap> split = new LinkedTreeMap<>();

        for (int i = 0, j = 0; i < this.qna.size(); i++, j++)
        {
            if (i + 1 == currentIndex)
            {
                j--;
                continue;
            }

            split.put(String.valueOf(j + 1), this.qna.get(String.valueOf(i + 1)));
        }

        this.qna = split;
    }

    public int getSize()
    {
        return this.qna.size();
    }

    public void saveJSON()
    {
        try {
            OutputStream outputStream = new FileOutputStream("q&a.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            new GsonBuilder().setPrettyPrinting().create().toJson(this.qna, bufferedWriter);
            bufferedWriter.close();
            System.out.println("JSON file saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
