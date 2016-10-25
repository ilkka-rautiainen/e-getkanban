import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import CardPhasePointRow from '../CardPhasePointRow/CardPhasePointRow'

const CardPhasePoints = ({ cardPhasePoints, cardPhasePointIds }) => {
    return <div className="card-phase-points">
        <CardPhasePointRow phaseName={"Analysis"} totalPoints={cardPhasePoints[cardPhasePointIds[0]].totalPoints}
                           pointsDone={cardPhasePoints[cardPhasePointIds[0]].pointsDone} />
        <CardPhasePointRow phaseName={"Development"} totalPoints={cardPhasePoints[cardPhasePointIds[1]].totalPoints}
                           pointsDone={cardPhasePoints[cardPhasePointIds[1]].pointsDone} />
        <CardPhasePointRow phaseName={"Test"} totalPoints={cardPhasePoints[cardPhasePointIds[2]].totalPoints}
                           pointsDone={cardPhasePoints[cardPhasePointIds[2]].pointsDone} />
    </div>
};

CardPhasePoints.propTypes = {
    cardPhasePoints: PropTypes.object.isRequired
};

const mapStateToProps = (state) => {
    return {
        cardPhasePoints: state.cardPhasePoints
    }
};

export default connect(
    mapStateToProps
)(CardPhasePoints);

