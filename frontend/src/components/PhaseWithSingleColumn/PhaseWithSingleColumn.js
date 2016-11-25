import React, { PropTypes } from 'react';
import ColumnCards from '../ColumnCards/ColumnCards';
import PhaseHeader from '../PhaseHeader/PhaseHeader';
import './PhaseWithSingleColumn.scss';

const PhaseWithSingleColumn = ({ phase, column }) => {
  return <div className="phase-with-single-column">
    <PhaseHeader
      id={ phase.id }
      name={ phase.name }
      wipLimit={ phase.wipLimit }
    />
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
