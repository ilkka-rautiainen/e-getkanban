import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import { Row } from 'react-flexbox-grid';
import PhaseContainer from '../PhaseContainer/PhaseContainer';
import './Board.scss';

class Board extends React.Component {
  static propTypes = {
    phases: PropTypes.objectOf(PropTypes.shape({
      id: PropTypes.string.isRequired,
      columns: PropTypes.array.isRequired
    }).isRequired).isRequired
  };

  constructor({ phases, totalColumns }) {
    super();
    this.phases = phases;
    this.totalColumns = totalColumns;
  }

  render() {
    return <div className="board">
      <Row className="phase-row">
        { Object.keys(this.phases).map(phaseId => {
          return <PhaseContainer
            key={phaseId}
            id={phaseId} />
        })}
      </Row>
    </div>
  }
}

const mapStateToProps = (state) => {
  return {
    phases: state.phases,
    totalColumns: state.columns.length
  }
};

export default connect(
  mapStateToProps
)(Board);
