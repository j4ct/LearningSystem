package cornelio;

import imgui.*;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.type.ImString;

import static imgui.flag.ImGuiWindowFlags.*;
import static imgui.flag.ImGuiConfigFlags.*;

public class GUI extends Application
{
    static int selected = 1;
    static boolean data_query = true;

    private ImString main_answer = new ImString();
    private ImString editQ = new ImString();
    private ImString editA = new ImString();

    private static QnA data = new QnA("q&a.txt");

    int random = (int) ((Math.random() * (data.getSize())) + 1);

    @Override
    protected void configure(Configuration config)
    {
        config.setTitle("Learning System");
        //System.out.println("once");

        config.setHeight(500);
        config.setWidth(500);
    }

    @Override
    protected void initImGui(final Configuration config)
    {
        super.initImGui(config);
        //System.out.println("once");

        final ImGuiIO io = ImGui.getIO();
        io.setIniFilename(null);               // Block init file.
        io.addConfigFlags(NavEnableKeyboard);  // Enable keyboard controls.
        io.addConfigFlags(DockingEnable);      // Enable docking.
        //io.addConfigFlags(ViewportsEnable);  // Enable Multi-Viewport / Platform Windows
        io.setConfigViewportsNoTaskBarIcon(true);
    }

    @Override
    protected void preProcess()
    {
        ImGuiViewport viewport = ImGui.getMainViewport();
        ImVec2 wndwSize = new ImVec2(viewport.getSizeX(), viewport.getSizeY());
        ImVec2 wndwPos = new ImVec2(viewport.getPosX(), viewport.getPosY());

        // Setting GUI size and location.
        //ImGui.setNextWindowSize(wndwSize.x, wndwSize.y);
        //ImGui.setNextWindowPos(wndwPos.x, wndwPos.y);
        ImGui.setWindowSize("main", wndwSize.x, wndwSize.y);
        ImGui.setWindowPos("main", wndwPos.x, wndwPos.y);

        ImGui.setWindowSize("edit", wndwSize.x, wndwSize.y);
        ImGui.setWindowPos("edit", wndwPos.x, wndwPos.y);
    }

    @Override
    public void process()
    {
        int flags = NoDecoration | NoMove | NoResize | NoSavedSettings;

        ImGui.begin("main", flags);

        //System.out.println("loop");

        {
            /* Example
            ImGui.text("asd");
            if (ImGui.button("\uf0c7" + " Save")) {
                count++;
            }
            ImGui.sameLine();
            ImGui.text(String.valueOf(count));

            ImGui.inputText("string", str, 1 << 18);
            ImGui.text("Result: " + str.get());
            ImGui.sliderFloat("float", flt, 0, 1);
            ImGui.separator();
            ImGui.text("Extra");
            */
            //ImGui.pushItemWidth(-1);
            ImGui.dummy(0f, ImGui.getContentRegionAvailY() * 0.35f);

            ImGui.text("Question: " + data.getQuestionByIndex(random));

            ImGui.text("Answer:");
            ImGui.sameLine();
            ImGui.inputText("", main_answer, 1 << 18);


            ImGui.sameLine();
            if (main_answer.toString().equals(data.getAnswerByIndex(random)))
            {
                ImGui.text("Correct!");
            }
            else
                ImGui.text("Incorrect");

            ImGui.spacing();

            if (ImGui.button("Get a new question"))
            {
                int prev = random;
                while (random == prev)
                    random = (int) ((Math.random() * (data.getSize())) + 1);

                main_answer.clear();
            }

            ImGui.sameLine();

            if (ImGui.button("Edit"))
                ImGui.openPopup("edit");

        }

        if (ImGui.beginPopupModal("edit", flags))
        {
            this.list_section();

            ImGui.sameLine();

            this.edit_section();

            ImGui.endPopup();
        }

        ImGui.end();
        //ImGui.showDemoWindow();
    }

    private void list_section()
    {
        ImGui.beginChild("list_section", ImGui.getContentRegionAvailX() * 0.5f, 0, false, HorizontalScrollbar);
        {
            for (int i = 1; i < data.getSize() + 1; i++)
            {
                if (ImGui.selectable("Question #" + i, selected == i))
                {
                    selected = i;
                    data_query = true;
                }

                //if (selected == i)
                //    ImGui.text("osito triste :(");

            }
        }
        ImGui.endChild();
    }

    private void edit_section()
    {
        ImGui.beginChild("edit_section", 0, 0, true, NoBackground);
        {
            ImGui.pushItemWidth(-1);

            if (data_query)
            {
                editQ.set(data.getQuestionByIndex(selected), true);
                editA.set(data.getAnswerByIndex(selected), true);

                data_query = false;
            }

            ImGui.text("Question");
            ImGui.inputText("", editQ );

            ImGui.spacing();

            ImGui.text("Answer");
            ImGui.inputText(" ", editA );

            ImGui.spacing();

            if (ImGui.button("Edit"))
            {
                data.setQuestionByIndex(selected, editQ.get());
                data.setAnswerByIndex(selected, editA.get());
                data.saveJSON();

                data_query = true;
            }


            if (ImGui.button("Close"))
            {
                ImGui.closeCurrentPopup();
                data_query = true;
            }

            ImGui.dummy(0f, ImGui.getContentRegionAvailY() * 0.05f);

            ImGui.text("Add or remove: ");
            ImGui.sameLine();
            if (ImGui.button("+"))
            {
                data.addByIndex(selected);

                //if (random == selected || random == data.getSize() - 1)
                //    random--;
                //else if (random == 1);

                selected++;

                data_query = true;
            }
            ImGui.sameLine();

            if (data.getSize() > 1)
                if (ImGui.button("-"))
                {
                    data.removeByIndex(selected);

                    /* getSize() + 1: because we already remove 1, and we need the prev size.
                       also, first the "random" if statement, because we need to compare the selected field with the current qna. */
                    if (random == selected || random == data.getSize() + 1)
                        random--;
                    else if (random == 1);

                    if (data.getSize() + 1 == selected)
                        selected--;

                    data_query = true;
                }
        }
        ImGui.endChild();
    }

    public static void displayGUI()
    {
        launch(new GUI());
    }
}
