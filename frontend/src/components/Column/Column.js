import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import ColumnCards from '../ColumnCards/ColumnCards';
import './Column.scss';

const Column = ({ column, borderRight, style }) => {
  if (column) {
    let borderRightStyle = {};
    if (borderRight) {
      borderRightStyle = style.borderRight;
    }
    return (
      <div className="column" style={borderRightStyle} >
        <div className="column-header">
          <div className="name" style={style.title} >{ column.name }</div>
        </div>
        <ColumnCards columnCardIds={column.cards} />
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
