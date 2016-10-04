import React, { PropTypes } from 'react';
import { Row, Col } from 'react-flexbox-grid';
import Column from '../Column/Column';
import './PhaseWithTwoColumns.scss';

export default class PhaseWithTwoColumns extends React.Component {
  static propTypes = {
    phase: PropTypes.shape({
      name: PropTypes.string.isRequired,
      columns: PropTypes.arrayOf(PropTypes.string.isRequired).isRequired,
    })
  };

  constructor({ phase }) {
    super();
    this.phase = phase;
  }

  get phaseHeaderStyle() {
    if (!this.phase.trackLine) {
      return {};
    }
    return {
      borderBottomColor: '#' + this.phase.trackLine.color
    };
  }

  render() {
    return <div className="PhaseWithTwoColumns">
      <div className="phase-header" style={this.phaseHeaderStyle}>
        <div className="align-wrapper">
          <div className="phase-name">{this.phase.name}</div>
          <div className="wip-limit">WIP {this.phase.wipLimit}</div>
        </div>
      </div>
      <Row className="column-row">
        {this.phase.columns.map(columnId =>
          <Col xs key={ columnId } className="column-col">
            <Column id={ columnId }/>
          </Col>
        )}
      </Row>
    </div>
  }
}
