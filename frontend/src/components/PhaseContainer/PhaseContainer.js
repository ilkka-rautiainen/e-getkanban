import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import { Col } from 'react-flexbox-grid';
import FontIcon from 'material-ui/FontIcon';
import _ from 'lodash';
import { setActiveDice, setActiveCard, assignResource, assignDie } from '../../actions';
import PhaseWithSingleColumn from '../PhaseWithSingleColumn/PhaseWithSingleColumn';
import PhaseWithTwoColumns from '../PhaseWithTwoColumns/PhaseWithTwoColumns';
import Die from "../Die/Die";
import constants from '../../constants';
import './PhaseContainer.scss';

class PhaseContainer extends React.Component {
  static propTypes = {
    phase: PropTypes.shape({
      id: PropTypes.string.isRequired,
      name: PropTypes.string.isRequired,
      columns: PropTypes.arrayOf(PropTypes.string.isRequired).isRequired
    }).isRequired,
    firstColumn: PropTypes.shape({
      id: PropTypes.string.isRequired,
    }),
  };

  constructor({ phase, phases, gameDifficulty, isFinalPhase, isFirstPhase }) {
    super();
    this.phase = phase;
    this.gameDifficulty = gameDifficulty;
    this.isFinalPhase = isFinalPhase;
    this.isFirstPhase = isFirstPhase;
    const otherWorkingPhases = _.pickBy(phases, function(value, key) {
      return value.isWorkPhase && value.id !== phase.id;
    });
    this.otherPhaseColors = Object.keys(otherWorkingPhases).map((phase) => {
      return otherWorkingPhases[phase].color
    });
  }

  handleDieClick(e, dieNumber) {
    if (this.gameDifficulty !== constants.GAME_DIFFICULTY_NORMAL) {
      if (this.props.selectedDice && this.props.selectedCard) {
        const diePhaseId = this.props.selectedDice.id;
        const diceIndex = this.props.selectedDice.diceIndex;
        const xMovement = this.props.selectedCard.cardPageX - this.props.selectedDice.diePageX;
        const yMovement = this.props.selectedCard.cardPageY - this.props.selectedDice.diePageY;
        const cardPhaseId = this.props.selectedCard.phaseId;
        const cardId = this.props.selectedCard.id;
        this.props.onCreateAssignResource(cardId, cardPhaseId, diePhaseId, diceIndex);
        this.props.onCreateAssignedDie(diePhaseId, diceIndex, xMovement, yMovement);
        this.props.onResetDiceAndCard();
      }
      const diePageX = e.nativeEvent.pageX;
      const diePageY = e.nativeEvent.pageY;
      if (this.props.assignedDice.filter((die) => die.index === dieNumber && die.phaseId === this.phase.id).length === 0) {
        this.props.onSetActiveDice(this.phase.id, dieNumber, diePageX, diePageY);
      }
    }
  }

  get className() {
    const baseClassNames = ['phase-container', 'phase-col'];
    const columnAmountBasedClassName = (this.phase.columns.length === 1) ? 'single-col' : 'two-cols';
    return [...baseClassNames, columnAmountBasedClassName].join(' ');
  }

  get dieStyle() {
    return {
      major: {
        color: "#"+this.phase.color
      },
      firstMinor: {
        color: "#"+this.otherPhaseColors[0]
      },
      secondMinor: {
        color: "#"+this.otherPhaseColors[1]
      }
    }
  }

  dieVisibilityClass(assignedClass) {
    let visibilityClass = this.props.showDice ? "visible" : "hidden";
    const dieNotAssigned = !this.props.showDice && !this.props.enableNextRound && assignedClass === "";
    if (dieNotAssigned) {
      visibilityClass = "visible";
    }
    return visibilityClass;
  }

  get dice() {
    let dice = [];
    if (this.props.phaseCardCount > 0 || this.props.dice) {
      for (var i = 0; i < this.props.phase.diceAmount; i++) {
        let selectedClass = "";
        let assignedClass = "";
        let diePosition = {};
        if (this.props.selectedDice && this.props.selectedDice.id === this.phase.id && this.props.selectedDice.diceIndex === i) {
          selectedClass = "selected";
          if (this.props.selectedDice && this.props.selectedCard) {
            const xMovement = this.props.selectedCard.cardPageX - this.props.selectedDice.diePageX;
            const yMovement = this.props.selectedCard.cardPageY - this.props.selectedDice.diePageY;
            diePosition = {
              top: yMovement,
              left: xMovement
            }
          }
        }
        if (this.props.assignedDice.length > 0) {
          const assignedDie = this.props.assignedDice.filter((die) => die.index === i && die.phaseId === this.phase.id)[0];
          if (assignedDie) {
            assignedClass = "assigned";
            diePosition = {
              top: assignedDie.yMovement,
              left: assignedDie.xMovement
            }
          }
        }
        const diceAssigned = this.props.dice && this.props.showDice && !this.props.enableNextRound && assignedClass !== "";
        const isNormalGameWithCardsInPhase = this.props.dice && this.gameDifficulty === constants.GAME_DIFFICULTY_NORMAL;
        const majorValue = (isNormalGameWithCardsInPhase|| diceAssigned) ? this.props.dice.dice[i].primaryValue : 0;
        const firstMinorValue = (isNormalGameWithCardsInPhase|| diceAssigned) ? this.props.dice.dice[i].firstSecondaryValue : 0;
        const secondMinorValue = (isNormalGameWithCardsInPhase|| diceAssigned) ? this.props.dice.dice[i].secondSecondaryValue : 0;
        const castNumbers = {
          firstMinor: firstMinorValue,
          major: majorValue,
          secondMinor: secondMinorValue
        };
        const dieNumber = i;
        const dieVisibilityClass = this.dieVisibilityClass(assignedClass);
        dice.push(<Die key={i} onClick={e => this.handleDieClick(e, dieNumber)}
                       castNumbers={castNumbers} visibilityClass={dieVisibilityClass} selectedClass={selectedClass}
                       dieStyle={this.dieStyle} diePosition={diePosition} assignedClass={assignedClass}
                       gameDifficulty={this.gameDifficulty} />);
      }
    }

    return dice;
  }

  get iconStyle() {
    return {
      position: "absolute",
      top: "5px",
      right: "10px",
      cursor: "help"
    }
  }

  get tooltipText() {
    const baseText = this.isFirstPhase ? "Next draw from backlog in " : "Next deploy in ";
    const daysUntilActionModulo = (this.props.gameCurrentDay) % this.props.firstColumn.dayMultiplierToEnter;
    let daysUntilAction;
    switch (daysUntilActionModulo) {
      case 0: daysUntilAction = 1; break;
      case 1: daysUntilAction = 3; break;
      case 2: daysUntilAction = 2; break;
    }
    const dayText = daysUntilAction === 1 ? " day" : " days";
    return baseText + daysUntilAction + dayText;
  }

  render() {
    if (!this.phase) {
      return null;
    }
    const isAdvancedGameFinalOrFirstPhase = this.gameDifficulty === constants.GAME_DIFFICULTY_ADVANCED && (this.isFinalPhase || this.isFirstPhase);
    return (
      <Col xs className={this.className}>
        { isAdvancedGameFinalOrFirstPhase && <FontIcon className="material-icons" style={this.iconStyle}>info</FontIcon> }
        { isAdvancedGameFinalOrFirstPhase && <span className="tooltip">{this.tooltipText}</span> }
        {this.dice}
        {this.phase.columns.length === 1 ?
          <PhaseWithSingleColumn
            phase={this.phase}
            column={this.props.firstColumn}
            isFinalPhase={this.props.isFinalPhase}
          /> :
          <PhaseWithTwoColumns phase={this.phase} />}
      </Col>
    )
  }
}

const mapStateToProps = (state, ownProps) => {
  const gameDifficulty = state.game.difficultyLevel;
  const phase = state.phases[ownProps.id];
  const phases = state.phases;
  const firstColumn = phase === undefined ? null : state.columns[phase.columns[0]];
  let dice = null;
  if (state.dice) {
    dice = state.dice.filter(function(diceAction) {return diceAction.phaseId === phase.id})[0];
  }
  let phaseCardCount = 0;
  phase.columns.map((columnId) => state.columns[columnId])
               .forEach((column) => phaseCardCount += column.cards.length);
  return {
    gameDifficulty: gameDifficulty,
    gameCurrentDay: state.game.currentDay,
    phase: phase,
    phases: phases,
    firstColumn: firstColumn,
    dice: dice,
    showDice: state.nextRoundUIState.showDice,
    enableNextRound: state.nextRoundUIState.enableButtonPress,
    isFinalPhase: phase.id === constants.DEPLOYED,
    isFirstPhase: phase.id === constants.ANALYSIS,
    selectedDice: state.phaseDice,
    selectedCard: state.activeCard,
    assignedDice: state.assignedDice,
    phaseCardCount: phaseCardCount
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    onSetActiveDice: (phaseId, dieIndex, diePageX, diePageY) => {
      dispatch(setActiveDice(phaseId, dieIndex, diePageX, diePageY));
      dispatch(setActiveDice(phaseId, dieIndex, diePageX, diePageY));
    },
    onResetDiceAndCard: () => {
      dispatch(setActiveDice(null));
      dispatch(setActiveCard(null));
    },
    onCreateAssignResource: (cardId, cardPhaseId, diePhaseId, dieIndex) => {
      dispatch(assignResource(cardId, cardPhaseId, diePhaseId, dieIndex))
    },
    onCreateAssignedDie: (phaseId, diceIndex, xMovement, yMovement) => {
      dispatch(assignDie(phaseId, diceIndex, xMovement, yMovement))
    }
  }
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PhaseContainer);
