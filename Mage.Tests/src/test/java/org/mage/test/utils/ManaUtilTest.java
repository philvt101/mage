package org.mage.test.utils;

import mage.abilities.Ability;
import mage.abilities.costs.mana.ManaCost;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.mana.*;
import mage.cards.Card;
import mage.cards.repository.CardRepository;
import mage.util.ManaUtil;
import org.junit.Assert;
import org.junit.Test;
import org.mage.test.serverside.base.CardTestPlayerBase;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * @author noxx
 */
public class ManaUtilTest extends CardTestPlayerBase {

    @Test
    public void test() {
        testManaToPayVsLand("{R}", "Blood Crypt", 2, 1); // should use {R}
        testManaToPayVsLand("{1}{R}", "Blood Crypt", 2, RedManaAbility.class); // should use {R}
        testManaToPayVsLand("{R}{B}", "Blood Crypt", 2, 2); // can't auto choose to pay
        testManaToPayVsLand("{2}{R}{B}", "Blood Crypt", 2, 2); // can't auto choose to pay
        testManaToPayVsLand("{R}{R}{B}{B}", "Blood Crypt", 2, 2); // can't auto choose to pay
        testManaToPayVsLand("{R}{G}{W}{W}{U}", "Blood Crypt", 2, RedManaAbility.class); // should use {R}
        testManaToPayVsLand("{R}{R}{G}{W}{W}{U}", "Blood Crypt", 2, RedManaAbility.class); // should use {R}
        testManaToPayVsLand("{R}{R}", "Blood Crypt", 2, RedManaAbility.class); // should use {R}
        testManaToPayVsLand("{G}{W}", "Blood Crypt", 2, 2); // can't auto choose to pay
        testManaToPayVsLand("{1}{G}{W}", "Blood Crypt", 2, 1); // should use any but auto choose it
        testManaToPayVsLand("{2}{G}{W}{U}", "Blood Crypt", 2, 1); // should use any but auto choose it
        testManaToPayVsLand("{3}", "Blood Crypt", 2, 1); // should use any but auto choose it

        testManaToPayVsLand("{R}{R}{G}{W}{W}{U}", "Watery Grave", 2, 1); // should use {U}
        testManaToPayVsLand("{R}{R}{G}{W}{W}", "Steam Vents", 2, 1); // should use {R}
        testManaToPayVsLand("{R}{R}{G}{B}{U}", "Temple Garden", 2, 1); // should use {G}
        testManaToPayVsLand("{W}{W}{G}{B}{U}", "Sacred Foundry", 2, 1); // should use {W}
        testManaToPayVsLand("{W}{W}{R}{B}{U}", "Overgrown Tomb", 2, BlackManaAbility.class); // should use {B}
        testManaToPayVsLand("{W}{W}{R}{B}{U}", "Swamp", 1, BlackManaAbility.class);
        testManaToPayVsLand("{W}{W}{R}{B}{U}", "Plains", 1, WhiteManaAbility.class);

        testManaToPayVsLand("{1}{R}", "Cavern of Souls", 2, 2); // can't auto choose to pay
        testManaToPayVsLand("{2}", "Cavern of Souls", 2, 2); // can't auto choose to pay

        // hybrid mana
        testManaToPayVsLand("{W/R}{W/R}{W/R}", "Sacred Foundry", 2, 1); // auto choose for hybrid mana: choose any
        testManaToPayVsLand("{R}{W/R}", "Sacred Foundry", 2, RedManaAbility.class); // auto choose for hybrid mana: we should choose {R}
        testManaToPayVsLand("{G}{W/R}", "Sacred Foundry", 2, 1); // auto choose for hybrid mana: choose any
        testManaToPayVsLand("{G}{W/R}{W}", "Sacred Foundry", 2, WhiteManaAbility.class); // auto choose for hybrid mana: choose {W}
        testManaToPayVsLand("{W/B}{W/B}", "Swamp", 1, BlackManaAbility.class);

        // we can't auto choose here:
        // let say we auto choose {R}, then we have to use it to pay for {R} not {W/R} (as {W/R} is more generic cost)
        // but in such case what is left to pay is {W/R}{W} and it is possible that we won't have 2 white sources
        // Example: 1x Sacred Foundry 1x Mountain 1x Mountain
        // we can pay {W/R}{W}{R} by using Sacred Foundry and choosing {W} then using two Mountains
        // but if we auto choose {R} then we won't be able to pay the cost at all
        testManaToPayVsLand("{W/R}{W}{R}", "Sacred Foundry", 2, 2);

        testManaToPayVsLand("{W/R}{R/G}", "Sacred Foundry", 2, 2); // can't auto choose to pay
    }

    /**
     * Common way to test ManaUtil.tryToAutoPay
     *
     * We get all mana abilities, then try to auto pay and compare to expected1 and expected2 params.
     *
     * @param manaToPay Mana that should be paid using land.
     * @param landName Land to use as mana producer.
     * @param expected1 The amount of mana abilities the land should have.
     * @param expected2 The amount of mana abilities that ManaUtil.tryToAutoPay should be returned after optimization.
     */
    private void testManaToPayVsLand(String manaToPay, String landName, int expected1, int expected2) {
        ManaCost unpaid = new ManaCostsImpl(manaToPay);
        Card card = CardRepository.instance.findCard(landName).getCard();
        Assert.assertNotNull(card);

        HashMap<UUID, ManaAbility> useableAbilities = getManaAbilities(card);
        Assert.assertEquals(expected1, useableAbilities.size());

        useableAbilities = ManaUtil.tryToAutoPay(unpaid, (LinkedHashMap<UUID, ManaAbility>)useableAbilities);
        Assert.assertEquals(expected2, useableAbilities.size());
    }

    /**
     * Another way to test ManaUtil.tryToAutoPay
     * Here we also check what ability was auto chosen
     *
     * N.B. This method can be used ONLY if we have one ability left that auto choose mode!
     * That's why we assert the following:
     * Assert.assertEquals(1, useableAbilities.size());
     *
     * We get all mana abilities, then try to auto pay and compare to expected1 and expected2 params.
     *
     * @param manaToPay Mana that should be paid using land.
     * @param landName Land to use as mana producer.
     * @param expected1 The amount of mana abilities the land should have.
     * @param expectedChosen
     */
    private void testManaToPayVsLand(String manaToPay, String landName, int expected1, Class<? extends BasicManaAbility> expectedChosen) {
        ManaCost unpaid = new ManaCostsImpl(manaToPay);
        Card card = CardRepository.instance.findCard(landName).getCard();
        Assert.assertNotNull(card);

        HashMap<UUID, ManaAbility> useableAbilities = getManaAbilities(card);
        Assert.assertEquals(expected1, useableAbilities.size());

        useableAbilities = ManaUtil.tryToAutoPay(unpaid, (LinkedHashMap<UUID, ManaAbility>)useableAbilities);
        Assert.assertEquals(1, useableAbilities.size());
        ManaAbility ability = useableAbilities.values().iterator().next();
        Assert.assertTrue("Wrong mana ability has been chosen", expectedChosen.isInstance(ability));
    }

    /**
     * Extracts mana abilities from the card.
     *
     * @param card Card to extract mana abilities from.
     * @return
     */
    private HashMap<UUID, ManaAbility> getManaAbilities(Card card) {
        HashMap<UUID, ManaAbility> useableAbilities = new LinkedHashMap<UUID, ManaAbility>();
        for (Ability ability: card.getAbilities()) {
            if (ability instanceof ManaAbility) {
                ability.newId(); // we need to assign id manually as we are not in game
                useableAbilities.put(ability.getId(), (ManaAbility)ability);
            }
        }
        return useableAbilities;
    }
}
