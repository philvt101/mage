/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */

package mage.sets.riseoftheeldrazi;

import mage.constants.CardType;
import mage.constants.Rarity;
import mage.MageInt;
import mage.abilities.Abilities;
import mage.abilities.AbilitiesImpl;
import mage.abilities.Ability;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.keyword.DeathtouchAbility;
import mage.abilities.keyword.FirstStrikeAbility;
import mage.abilities.keyword.LevelUpAbility;
import mage.abilities.keyword.LevelerCardBuilder;
import mage.cards.LevelerCard;

import java.util.UUID;

/**
 *
 * @author Loki, noxx
 */
public class NirkanaCutthroat extends LevelerCard {

    public NirkanaCutthroat (UUID ownerId) {
        super(ownerId, 119, "Nirkana Cutthroat", Rarity.UNCOMMON, new CardType[]{CardType.CREATURE}, "{2}{B}");
        this.expansionSetCode = "ROE";
        this.subtype.add("Vampire");
        this.subtype.add("Warrior");
        this.color.setBlack(true);
        this.power = new MageInt(3);
        this.toughness = new MageInt(2);
        this.addAbility(new LevelUpAbility(new ManaCostsImpl("{2}{B}")));

        Abilities<Ability> abilities1 = new AbilitiesImpl<Ability>();
        abilities1.add(DeathtouchAbility.getInstance());

        Abilities<Ability> abilities2 = new AbilitiesImpl<Ability>();
        abilities2.add(FirstStrikeAbility.getInstance());
        abilities2.add(DeathtouchAbility.getInstance());

        LevelerCardBuilder.construct(this,
                new LevelerCardBuilder.LevelAbility(1, 2, abilities1, 4, 3),
                new LevelerCardBuilder.LevelAbility(3, -1, abilities2, 5, 4)
        );
    }

    public NirkanaCutthroat (final NirkanaCutthroat card) {
        super(card);
    }

    @Override
    public NirkanaCutthroat copy() {
        return new NirkanaCutthroat(this);
    }

}
