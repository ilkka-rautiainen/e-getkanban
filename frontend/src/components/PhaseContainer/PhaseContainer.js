import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import { Col } from 'react-flexbox-grid';
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

  constructor({ phase, firstColumn }) {
    super();
    this.phase = phase;
  }

  get className() {
    const baseClassNames = ['phase-container', 'phase-col'];
    const columnAmountBasedClassName = (this.phase.columns.length === 1) ? 'single-col' : 'two-cols';
    return [...baseClassNames, columnAmountBasedClassName].join(' ');
  }

  get dieStyle() {
    return {
      color: "#"+this.phase.color
    }
  }

  get dieAddedClass() {
    return this.props.showDice ? "visible" : "hidden";
  }

  get dice() {
    let dice = [];
    for (var i = 0; i < this.props.dice.diceValues.length; i++) {
      const dieValue = this.props.dice.diceValues[i];
      dice.push(<Die key={i} castNumber={dieValue} addedClass={this.dieAddedClass} dieStyle={this.dieStyle} />)
    }
    return dice;
  }

  render() {
    if (!this.phase) {
      return null;
    }
    return (
      <Col xs className={this.className}>
        {this.props.dice && this.dice}
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
  const phase = state.phases[ownProps.id];
  const firstColumn = phase === undefined ? null : state.columns[phase.columns[0]];
  const isFinalPhase = phase.id === constants.DEPLOYED;
  let dice = null;
  if (state.game.lastTurn && state.game.lastTurn.diceCastActions) {
    dice = state.game.lastTurn.diceCastActions.filter(function(diceAction) {return diceAction.phaseId === phase.id})[0];
  }
  return {
    phase: phase,
    firstColumn: firstColumn,
    dice: dice,
    showDice: state.nextRoundUIState.showDice,
    isFinalPhase: isFinalPhase
  };
};

export default connect(
  mapStateToProps
)(PhaseContainer);
