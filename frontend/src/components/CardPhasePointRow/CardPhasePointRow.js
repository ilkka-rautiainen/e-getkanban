import React, { PropTypes } from 'react';
import _ from 'lodash';
import "./CardPhasePointRow.scss"

const PHASE_POINT_DIV_AMOUNT = 8;

const CardPhasePointRow = ({ phaseName, totalPoints, pointsDone, color }) => {
  return <div className="phase-point-row" style={{backgroundColor: '#' + color}}>
    <div className="phase-name">{phaseName}</div>
    <div className="phase-points">
      {_.times(PHASE_POINT_DIV_AMOUNT, i =>
        <span key={i} className="phase-point-block">&nbsp;</span>
      )}
      <div className="total-points">
        {_.times(totalPoints, i =>
          <span key={i}>&nbsp;</span>
        )}
      </div>
      <div className="points-done">
        {_.times(pointsDone, i =>
          <span key={i}></span>
        )}
      </div>
    </div>
  </div>
};

CardPhasePointRow.propTypes = {
  phaseName: PropTypes.string.isRequired,
  totalPoints: PropTypes.number.isRequired,
  pointsDone: PropTypes.number
};

export default CardPhasePointRow;


