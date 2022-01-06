package at.xirado.interact.event.events;

import at.xirado.interact.Interact;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.interactions.InteractionImpl;
import net.dv8tion.jda.internal.utils.Checks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ApplicationCommandAutocompleteEvent extends InteractionEvent
{
    public ApplicationCommandAutocompleteEvent(Interact interact, InteractionImpl interaction)
    {
        super(interact, interaction);
        System.out.println("Creating new autocomplete event!");
    }

    public void sendChoices(Collection<Command.Choice> choices)
    {
        DataArray choicesArray = DataArray.empty();
        choices.forEach(choice -> choicesArray.add(DataObject.empty().put("name", choice.getName()).put("value", choice.getAsString())));
        DataObject data = DataObject.empty().put("choices", choicesArray);
        DataObject body = DataObject.empty()
                .put("type", 8)
                .put("data", data);
        respond(body);
    }

    public void sendChoices(Command.Choice choice, Command.Choice... choices)
    {
        Checks.notNull(choice, "Choice");
        Checks.noneNull(choices, "Choices");
        List<Command.Choice> choicesList = new ArrayList<>();
        choicesList.add(choice);
        Collections.addAll(choicesList, choices);
        sendChoices(choicesList);
    }
}
