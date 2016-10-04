import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import ColumnCards from '../ColumnCards/ColumnCards';
import './Column.scss';

const Column = ({ column }) => {
  return <div className="Column">
    <div className="column-header">
      <div className="name">{ column.name }</div>
    </div>
    <ColumnCards columnCardIds={column.cards} />
  </div>
};

Column.propTypes = {
  column: PropTypes.shape({
    name: PropTypes.string.isRequired
  })
};

const mapStateToProps = (state, ownProps) => {
  return {
    column: state.columns[ownProps.id]
  };
};

export default connect(
  mapStateToProps
)(Column);
