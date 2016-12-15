import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import _ from 'lodash';
import CardPhasePointRow from '../CardPhasePointRow/CardPhasePointRow';

const CardPhasePoints = ({ cardPhasePoints, cardPhasePointIds, workingPhases }) => {
  let rows = [];
  for (var i = 0; i < Object.keys(workingPhases).length; i++) {
    rows.push(
        <CardPhasePointRow key={i} phaseName={workingPhases[Object.keys(workingPhases)[i]].shortName}
                           totalPoints={cardPhasePoints[cardPhasePointIds[i]].totalPoints}
                           pointsDone={cardPhasePoints[cardPhasePointIds[i]].pointsDone}
                           color={workingPhases[Object.keys(workingPhases)[i]].color}/>
    );
  }
  return <div className="card-phase-points">{rows}</div>
};

CardPhasePoints.propTypes = {
  cardPhasePoints: PropTypes.object.isRequired
};

const mapStateToProps = (state) => {
  const workingPhases = _.pickBy(state.phases, function(value, key) {return value.isWorkPhase;});
  return {
    cardPhasePoints: state.cardPhasePoints,
    workingPhases: workingPhases
  }
};

export default connect(
  mapStateToProps
)(CardPhasePoints);

