import React, { PropTypes } from 'react';
import { Row, Col } from 'react-flexbox-grid';
import Column from '../Column/Column';
import PhaseHeader from '../PhaseHeader/PhaseHeader';
import './PhaseWithTwoColumns.scss';

export default class PhaseWithTwoColumns extends React.Component {
  static propTypes = {
    phase: PropTypes.shape({
      name: PropTypes.string.isRequired,
      columns: PropTypes.arrayOf(PropTypes.string.isRequired).isRequired,
    }).isRequired
  };

  constructor({ phase }) {
    super();
    this.phase = phase;
  }

  get titleStyle() {
    if (!this.phase.color) {
      return {};
    }
    return {
      color: '#' + this.phase.color
    };
  }

  get borderBottomStyle() {
    if (!this.phase.color) {
      return {};
    }
    return {
      borderBottomColor: '#' + this.phase.color
    };
  }

  get borderRightStyle() {
    if (!this.phase.color) {
      return {};
    }
    return {
      borderRightColor: '#' + this.phase.color
    };
  }

  render() {
    return <div className="phase-with-two-columns">
      <PhaseHeader
        id={this.phase.id}
        name={this.phase.name}
        wipLimit={this.phase.wipLimit}
        titleStyle={this.titleStyle}
        borderBottomStyle={this.borderBottomStyle}
      />
      <Row className="column-row">
        {this.phase.columns.map((columnId, index) =>
          <Col xs key={columnId} className="column-col">
            <Column id={columnId}
                    phase={this.phase}
                    showBorderRight={index===0}
                    titleStyle={this.titleStyle}
                    borderRightStyle={this.borderRightStyle}
            />
          </Col>
        )}
      </Row>
    </div>
  }
}
