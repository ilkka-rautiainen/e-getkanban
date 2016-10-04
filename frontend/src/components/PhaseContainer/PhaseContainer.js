import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import { Col } from 'react-flexbox-grid';
import PhaseWithSingleColumn from '../PhaseWithSingleColumn/PhaseWithSingleColumn';
import PhaseWithTwoColumns from '../PhaseWithTwoColumns/PhaseWithTwoColumns';
import './PhaseContainer.scss';

class PhaseContainer extends React.Component {
  static propTypes = {
    phase: PropTypes.shape({
      id: PropTypes.string.isRequired,
      name: PropTypes.string.isRequired,
      columns: PropTypes.arrayOf(PropTypes.string.isRequired).isRequired
    }).isRequired,
    firstColumn: PropTypes.shape({
      id: PropTypes.string.isRequired,
    }),
  };

  constructor({ phase, firstColumn }) {
    super();
    this.phase = phase;
    this.firstColumn = firstColumn;
  }

  get className() {
    const baseClassNames = ['PhaseContainer', 'phase-col'];
    const columnAmountBasedClassName = (this.phase.columns.length === 1) ? 'single-col' : 'two-cols';
    return [...baseClassNames, columnAmountBasedClassName].join(' ');
  }

  render() {
    return <Col xs className={this.className}>
      {this.phase.columns.length === 1 ?
        <PhaseWithSingleColumn phase={this.phase} column={this.firstColumn} /> :
        <PhaseWithTwoColumns phase={this.phase} />}
    </Col>
  }
}

const mapStateToProps = (state, ownProps) => {
  const phase = state.phases[ownProps.id];
  return {
    phase: phase,
    firstColumn: state.columns[phase.columns[0]]
  };
};

export default connect(
  mapStateToProps
)(PhaseContainer);
