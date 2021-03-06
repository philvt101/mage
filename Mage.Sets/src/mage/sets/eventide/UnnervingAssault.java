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
package mage.sets.eventide;

import java.util.UUID;
import mage.abilities.condition.common.ManaWasSpentCondition;
import mage.abilities.decorator.ConditionalContinousEffect;
import mage.abilities.effects.common.continious.BoostAllEffect;
import mage.cards.CardImpl;
import mage.constants.CardType;
import mage.constants.ColoredManaSymbol;
import mage.constants.Duration;
import mage.constants.ManaType;
import mage.constants.Rarity;
import mage.constants.TargetController;
import mage.filter.common.FilterCreaturePermanent;
import mage.filter.predicate.permanent.ControllerPredicate;
import mage.watchers.common.ManaSpentToCastWatcher;

/**
 *
 * @author jeffwadsworth

 */
public class UnnervingAssault extends CardImpl {
    
    private static final FilterCreaturePermanent filter = new FilterCreaturePermanent("Creatures your opponents control");
    private static final FilterCreaturePermanent filter2 = new FilterCreaturePermanent("creatures you control");
    
    static {
        filter.add(new ControllerPredicate(TargetController.OPPONENT));
        filter2.add(new ControllerPredicate(TargetController.YOU));
    }

    public UnnervingAssault(UUID ownerId) {
        super(ownerId, 114, "Unnerving Assault", Rarity.UNCOMMON, new CardType[]{CardType.INSTANT}, "{2}{U/R}");
        this.expansionSetCode = "EVE";

        this.color.setRed(true);
        this.color.setBlue(true);

        // Creatures your opponents control get -1/-0 until end of turn if {U} was spent to cast Unnerving Assault, and creatures you control get +1/+0 until end of turn if {R} was spent to cast it.
        this.getSpellAbility().addEffect(new ConditionalContinousEffect(
                new BoostAllEffect(-1, 0, Duration.EndOfTurn, filter, false),
                new ManaWasSpentCondition(ColoredManaSymbol.U), "Creatures your opponents control get -1/0 until end of turn if {U} was spent to cast {this},"));
        this.getSpellAbility().addEffect(new ConditionalContinousEffect(
                new BoostAllEffect(1, 0, Duration.EndOfTurn, filter2, false),
                new ManaWasSpentCondition(ColoredManaSymbol.R), " and creatures you control get +1/0 until end of turn if {R} was spent to cast it"));
        this.addInfo("Info1", "<i>(Do both if {U}{R} was spent.)</i>");
        this.addWatcher(new ManaSpentToCastWatcher());
        
    }

    public UnnervingAssault(final UnnervingAssault card) {
        super(card);
    }

    @Override
    public UnnervingAssault copy() {
        return new UnnervingAssault(this);
    }
}

