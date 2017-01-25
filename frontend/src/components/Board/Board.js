import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import { Row } from 'react-flexbox-grid';
import PhaseContainer from '../PhaseContainer/PhaseContainer';
import './Board.scss';

const Board = ({ phases, enteredBoardTrackLineColor }) => {
  const boardStyle = {
    borderLeftColor: "#"+enteredBoardTrackLineColor
  }
  return <div className="board" style={boardStyle}>
      <Row className="phase-row">
        { phases.map(phaseId => {
          return <PhaseContainer
            key={phaseId}
            id={phaseId} />
        })}
      </Row>
  </div>
};

Board.propTypes = {
  phases: PropTypes.arrayOf(PropTypes.string).isRequired,
  enteredBoardTrackLineColor: PropTypes.string.isRequired
};

const mapStateToProps = (state) => {
  return {
    phases: state.board.phases,
    enteredBoardTrackLineColor: state.board.enteredBoardTrackLineColor
  }
};

export default connect(
  mapStateToProps
)(Board);
