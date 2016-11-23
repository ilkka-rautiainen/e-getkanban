import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import ColumnCards from '../ColumnCards/ColumnCards';
import './Column.scss';

const Column = ({ column }) => {
  if (column) {
    return <div className="column">
      <div className="column-header">
        <div className="name">{ column.name }</div>
      </div>
      <ColumnCards columnCardIds={column.cards} />
    </div>
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
