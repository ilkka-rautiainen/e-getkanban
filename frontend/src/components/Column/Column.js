import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import ColumnCards from '../ColumnCards/ColumnCards';
import './Column.scss';

const Column = ({ column, phase, showBorderRight, titleStyle, borderRightStyle }) => {
  if (column) {
    let borderStyle = {};
    if (showBorderRight) {
      borderStyle = borderRightStyle;
    }
    return (
      <div className="column" style={borderStyle} >
        <div className="column-header">
          <div className="name" style={titleStyle} >{column.name}</div>
        </div>
        <ColumnCards columnCardIds={column.cards} phase={phase} />
      </div>
    )
  } else {
    return null;
  }
};

Column.propTypes = {
  column: PropTypes.shape({
    name: PropTypes.string.isRequired
  }).isRequired
};

const mapStateToProps = (state, ownProps) => {
  return {
    column: state.columns[ownProps.id]
  };
};

export default connect(
  mapStateToProps
)(Column);
