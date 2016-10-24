import React, { PropTypes } from 'react';
import ColumnCards from '../ColumnCards/ColumnCards';
import './PhaseWithSingleColumn.scss';

const PhaseWithSingleColumn = ({ phase, column }) => {
  return <div className="phase-with-single-column">
    <div className="phase-header">
      <div className="alignment-wrapper">
        <div className="phase-name">{phase.name}</div>
        <div className="wip-limit">WIP {phase.wipLimit}</div>
      </div>
    </div>
    <div className="column-cards">
      <ColumnCards columnCardIds={column.cards} />
    </div>
  </div>
};

PhaseWithSingleColumn.propTypes = {
  phase: PropTypes.shape({
    name: PropTypes.string.isRequired,
  }),
  column: PropTypes.shape({
    cards: PropTypes.arrayOf(PropTypes.string.isRequired).isRequired
  }),
};

export default PhaseWithSingleColumn;
