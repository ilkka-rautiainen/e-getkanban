import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import { Row } from 'react-flexbox-grid';
import PhaseContainer from '../PhaseContainer/PhaseContainer';
import './Board.scss';

const Board = ({ phases }) => {
  return <div className="board">
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
  phases: PropTypes.arrayOf(PropTypes.string).isRequired
};

const mapStateToProps = (state) => {
  return {
    phases: state.board.phases,
    totalColumns: state.columns.length
  }
};

export default connect(
  mapStateToProps
)(Board);
