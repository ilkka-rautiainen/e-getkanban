import React, {PropTypes } from 'react';
import ColumnCards from '../ColumnCards/ColumnCards';
import PhaseHeader from '../PhaseHeader/PhaseHeader';
import './PhaseWithSingleColumn.scss';

const PhaseWithSingleColumn = ({phase, column, isFinalPhase}) => {

  let phaseHeaderStyle = {};
  if (phase.color) {
    phaseHeaderStyle = {
      color: '#' + phase.color
    };
  }
  let phaseStyle = {};
  if (isFinalPhase) {
    phaseStyle = {
      borderLeft: "2px solid #" + phase.color
    }
  }

  return <div className="phase-with-single-column" style={phaseStyle}>
    <PhaseHeader
      id={phase.id}
      name={phase.name}
      wipLimit={phase.wipLimit}
      titleStyle={phaseHeaderStyle}
    />
    <ColumnCards columnCardIds={column.cards} phase={phase} />
  </div>
};

PhaseWithSingleColumn.propTypes = {
  phase: PropTypes.shape({
    name: PropTypes.string.isRequired,
  }),
  column: PropTypes.shape({
    cards: PropTypes.arrayOf(PropTypes.string.isRequired).isRequired
  }),
  isFinalPhase: PropTypes.bool.isRequired
};

export default PhaseWithSingleColumn;
