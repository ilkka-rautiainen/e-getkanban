import React, { PropTypes } from 'react';
import _ from 'lodash';

const PHASE_POINT_DIV_AMOUNT = 8;

const CardPhasePointRow = ({ phaseName, totalPoints, pointsDone }) => {
    return <div className="phase-point-row">
            <div>{phaseName}</div>
            <div className="phase-points">
                {_.times(PHASE_POINT_DIV_AMOUNT, i =>
                    <span key={i}>{i}</span>
                )}
                {_.times(totalPoints, i =>
                    <span key={i}>{i}</span>
                )}
                {_.times(pointsDone, i =>
                    <span key={i}>{i}</span>
                )}
            </div>
        </div>
};

CardPhasePointRow.propTypes = {
    phaseName: PropTypes.string.isRequired,
    totalPoints: PropTypes.number.isRequired,
    pointsDone: PropTypes.number
};

export default CardPhasePointRow;


