import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import Card from '../Card/Card';
import "./BacklogDeck.scss";

const BacklogDeck = ({ topMostBacklogCardId }) => {
  return <div className="backlog">
      <Card key={topMostBacklogCardId} id={topMostBacklogCardId} />
  </div>
};

BacklogDeck.propTypes = {
  topMostBacklogCardId: PropTypes.string.isRequired
};

const mapStateToProps = (state) => {
  return {
    topMostBacklogCardId: state.backlogDeck[0]
  }
};

export default connect(
  mapStateToProps
)(BacklogDeck);
