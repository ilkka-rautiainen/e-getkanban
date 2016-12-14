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

  get childStyle() {
    if (!this.phase.color) {
      return {};
    }
    return {
      border: {
        borderBottomColor: '#' + this.phase.color
      },
      title: {
        color: '#' + this.phase.color
      },
      borderRight: {
        borderRightColor: '#' + this.phase.color
      }
    };
  }

  render() {
    return <div className="phase-with-two-columns">
      <PhaseHeader
        id={ this.phase.id }
        name={ this.phase.name }
        wipLimit={ this.phase.wipLimit }
        style={ this.childStyle }
      />
      <Row className="column-row">
        {this.phase.columns.map((columnId, index) =>
          <Col xs key={ columnId } className="column-col">
            <Column id={ columnId } borderRight={index===0} style={ this.childStyle } />
          </Col>
        )}
      </Row>
    </div>
  }
}
